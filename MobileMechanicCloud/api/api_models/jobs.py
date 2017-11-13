from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify, request
from database.job_request import JobRequestDAO
from database.user_request import UsersDAO
from extensions import mongo

class JobAPI(Resource):

    def __init__(self):
        self.jobsDAO = JobRequestDAO(mongo)
        self.usersDAO = UsersDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('job', type=dict, location='json')
        self.reqparse.add_argument('job_id', type=str, location='args')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)
        args = self.reqparse.parse_args()
        job_id = args.get('job_id')
        if job_id:
            job = self.jobsDAO.find_job(user_id, job_id)
            if job:
                return jsonify(job.__dict__)
            else:
                abort(401)
        else:
            jobs_list = self.jobsDAO.find_job(user_id)
            jobs_list = list([i.__dict__ for i in jobs_list])
            return jsonify(jobs_list)

    # TODO: Enforce a already exists check, so that users use PUT to update instead
    @jwt_required()
    def post(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)
        args = self.reqparse.parse_args()
        job_inserted = args.get('job')
        if not job_inserted:
            abort(401)
        if not all(list(map(job_inserted.get, ['make', 'model', 'year', 'options', 'summary', 'description', 'status']))):
            abort(401)
        if not type(job_inserted['options']) is dict:
            abort(401)
        insertion_successful, job_id = self.jobsDAO.insert_job(user_id,
                                        job_inserted['make'], job_inserted['model'],
                                        job_inserted['year'], job_inserted['options'],
                                        job_inserted['summary'],
                                        job_inserted['description'],
                                        job_inserted['status'])
        if insertion_successful:
            job = self.jobsDAO.find_job(user_id, job_id)
            return jsonify(results=job.__dict__)
        # give job id in return
        else:
            abort(401)

    # TODO: check the dictionary for malicious fields and for whether only true false values are inserted
    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != str(current_identity._get_current_object().id.encode('utf8'), encoding='utf-8'):
            abort(401)

        args = self.reqparse.parse_args()
        job_data = args.get('job')
        job_id = args.get('job_id')
        if not job_data or not job_data.get('updated_values') or not job_id:
            abort(401)
        updated_values = {str(k): str(v) for k, v in job_data['updated_values'].items()}
        update_successful = self.jobsDAO.update_job(user_id, job_id, updated_values)
        if update_successful:
            job = self.jobsDAO.find_job(user_id, job_id)
            return jsonify(results=job.__dict__)
        else:
            abort(404)

    @jwt_required()
    def delete(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        job_id = args.get('job_id')
        if job_id:
            delete_successful = self.jobsDAO.delete_one(user_id, job_id)
            if delete_successful:
                return jsonify({"success": "Deleted succesfully!"})
            else:
                return jsonify({"success": "Job not found"})
        else:
            abort(404)
