from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify, request, current_app
from database.job_request import JobRequestDAO
from database.user_request import UserDAO
from database.mechanic_request import MechanicDAO
from extensions import mongo
from configuration import LOGGING_JSON
import logging.config

logging.config.dictConfig(LOGGING_JSON)
from pyfcm import FCMNotification
from enums.job_status import JobStatus

class JobAPI(Resource):

    def __init__(self):
        self.jobsDAO = JobRequestDAO(mongo)
        self.userDAO = UserDAO(mongo)
        self.mechanicDAO = MechanicDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('job', type=dict, location='json')
        self.reqparse.add_argument('job_id', type=str, location='args')
        self.push_service = FCMNotification(api_key=current_app.config.get('FCM_SERVER_KEY'))

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        job_id = args.get('job_id')
        if job_id:
            job = self.jobsDAO.find_job(user_id=user_id, job_id=job_id)
            if job:
                return job.as_dict()
            abort(401)
        else:
            mechanic = self.mechanicDAO.find_mechanic(user_id)
            if mechanic is None:
                jobs_list = self.jobsDAO.find_job(user_id=user_id)
            else:
                jobs_list = self.jobsDAO.find_job(zipcodes=mechanic.serving_zipcodes)

            return jsonify([i.as_dict() for i in jobs_list])

    @jwt_required()
    def post(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        job_inserted = args.get('job')
        # if there is a problem with the inserted job format
        if not job_inserted:
            logging.debug('"job" key missing in inserted job.')
            abort(400)
        if not all(list(map(job_inserted.get, ['make', 'model', 'year', 'options', 'summary',
                                               'description', 'status', 'address_line', 'city',
                                               'state', 'zipcode']))):
            logging.debug('Job missing a few parameters.')
            abort(400)
        if not type(job_inserted['options']) is dict:
            logging.debug('"options" field is not dict')
            abort(400)

        insertion_successful, job_id = self.jobsDAO.insert_job(user_id,
                                                               job_inserted['make'], job_inserted['model'],
                                                               job_inserted['year'], job_inserted['options'],
                                                               job_inserted['summary'], job_inserted['description'],
                                                               JobStatus.submitted.name, job_inserted['address_line'],
                                                               job_inserted['city'], job_inserted['state'],
                                                               job_inserted['zipcode'])
        if not insertion_successful:
            abort(500)
        job = self.jobsDAO.find_job(user_id, job_id)
        if not job:
            abort(404)
        self.send_notifications(job_inserted)
        return job.as_dict()

    # TODO: implement sending notifications as Asynchronous process
    def send_notifications(self, job_inserted):
        registration_ids = self.mechanicDAO.get_relevant_mechanic_tokens(job_inserted['zipcode'])
        logging.debug("Got registration ids for job -> " + str(registration_ids))
        message_title = "New job"
        message_body = "New job available in your area"
        result = self.push_service.notify_multiple_devices(registration_ids=registration_ids,
                                                           message_title=message_title,
                                                           message_body=message_body)
        logging.debug('Notification result is ' + str(result))

    # TODO: check the dictionary for malicious fields and for whether only true false values are inserted
    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        job_data = args.get('job')
        job_id = args.get('job_id')
        logging.debug('Current job_data is' + str(job_data))
        logging.debug('Current job_id is ' + str(job_id))
        if not job_data:
            logging.debug('Job data missing.')
            abort(401, description='Job data field is missing')
        if not job_data.get('updated_values'):
            logging.debug('"updated_values" field is missing.')
            abort(401, description='updated_values field is missing.')
        if not job_id:
            logging.debug('Could not find a job of that given id.')
            abort(404)

        # updated_values = {str(k): str(v) for k, v in job_data['updated_values'].items()}
        updated_values = job_data.get('updated_values')
        update_successful = self.jobsDAO.update_job(user_id, job_id, updated_values)
        if not update_successful:
            abort(404)
        job = self.jobsDAO.find_job(user_id, job_id)
        if not job:
            logging.debug('Job not found')
            abort(404)
        return job.as_dict()

    @jwt_required()
    def delete(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        job_id = args.get('job_id')
        if not job_id:
            logging.debug('Cannot find job_id')
            abort(404)
        delete_successful = self.jobsDAO.delete_one(user_id, job_id)
        if not delete_successful:
            return jsonify({"failure": "Job not found."})
        return jsonify({"success": "Job deleted succesfully."})
