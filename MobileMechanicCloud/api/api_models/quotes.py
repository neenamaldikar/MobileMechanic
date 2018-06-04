from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify, request, current_app
from database.quote_request import QuoteRequestDAO
from database.user_request import UserDAO
from database.mechanic_request import MechanicDAO
from database.token_request import TokenDAO
from extensions import mongo
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)
from pyfcm import FCMNotification

class QuotesAPI(Resource):

    def __init__(self):
        self.quotesDAO = QuoteRequestDAO(mongo)
        self.userDAO = UserDAO(mongo)
        self.mechanicDAO = MechanicDAO(mongo)
        self.tokenDAO = TokenDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('quote', type=dict, location='json')
        self.reqparse.add_argument('quote_id', type=str, location='args')
        self.push_service = FCMNotification(api_key=current_app.config.get('FCM_SERVER_KEY'))

    @jwt_required()
    def get(self, user_id, job_id):
        user_id = str(user_id)
        is_mechanic = self.current_user_is_mechanic()
        if is_mechanic:
            mechanic_id = current_identity._get_current_object().id
        else:
            mechanic_id = None

        if (user_id != current_identity._get_current_object().id) or (not mechanic_id):
            abort(401, description="Unauthorized, not a mechanic or owner")

        args = self.reqparse.parse_args()
        quote_id = args.get('quote_id')
        if quote_id:
            quote = self.quotesDAO.find_quote(quote_id=quote_id, job_id=job_id, mechanic_id=mechanic_id)
            if quote:
                return quote.as_dict()
            abort(401)
        else:
            quotes_list = self.quotesDAO.find_quotes(user_id=user_id, job_id=job_id,
                                                     mechanic_id=mechanic_id)
            return jsonify([i.as_dict() for i in quotes_list])

    @jwt_required()
    def post(self, user_id,job_id):
        user_id = str(user_id)

        if not self.current_user_is_mechanic():
            abort(401, description="User is not a mechanic")

        job = self.jobsDAO.find_job(user_id=user_id, job_id=job_id)
        if job:
            job = job.as_dict()
        else:
            abort(404, description='Job not found')

        args = self.reqparse.parse_args()
        quote_details = args.get('quote')
        if not quote_details:
            logging.debug('"quote" key missing in inserted job.')
            abort(400)

        if not all(list(map(quote_details.get,
                            ['labor_cost', 'part_cost', 'onsite_service_charges']))):
            logging.debug('Quote missing a few parameters')
            abort(400)
        if not type(quote_details['labor_cost']) is dict:
            logging.debug('"labor_cost" field is not dict')
            abort(400)
        if not type(quote_details['part_cost']) is dict:
            logging.debug('"labor_cost" field is not dict')
            abort(400)

        insertion_successful, quote_id = self.quotesDAO.insert_quote(job_id,
                                        quote_details['make'], quote_details['model'],
                                        quote_details['year'], quote_details['options'],
                                        quote_details['summary'],quote_details['description'],
                                        quote_details['status'], quote_details['address_line'],
                                        quote_details['city'], quote_details['state'],
                                        quote_details['zipcode'])
        if not insertion_successful:
            abort(500)
        job = self.jobsDAO.find_job(user_id, job_id)
        if not job:
            abort(404)
        self.send_notifications(quote_details)
        return job.as_dict()

    def current_user_is_mechanic(self):
        mechanic_id = current_identity._get_current_object().id
        mechanic = self.mechanicDAO.find_mechanic(mechanic_id)
        if mechanic is None:
            return False
        else:
            return True

    def send_notifications(self, customer_user_id):
        registration_ids = self.tokenDAO.find_user_token(user_id=customer_user_id)
        message_title = "New quote"
        message_body = "New quote available for your job"
        result = self.push_service.notify_single_device(registration_id=registration_ids,
                                                  message_title=message_title,
                                                  message_body=message_body)
        logging.debug('Notification result is ' + str(result))
'''
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

        #updated_values = {str(k): str(v) for k, v in job_data['updated_values'].items()}
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
    def delete(self, user_id, job_id):
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
'''
