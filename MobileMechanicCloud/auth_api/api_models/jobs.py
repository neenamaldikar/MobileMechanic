from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify, request
from database.job_request import JobRequestDAO
from database.user_request import UsersDAO
from extensions import mongo
from bson import Binary
from gridfs import GridFS
import tempfile

class JobAPI(Resource):

    def __init__(self):
        self.jobsDAO = JobRequestDAO(mongo)
        self.usersDAO = UsersDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('job', type=dict, location='json')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        # the args should contain job id for a specific use case
        # args = self.reqparse.parse_args()
        # print ('Get request args are', args)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        jobs_list = self.jobsDAO.find_job(user_id)
        print('jobs_list is', jobs_list)
        print(jobs_list)
        if jobs_list is None:
            abort(404)
        final_list = []
        for i in jobs_list:
            final_list.append(i.__dict__)
        return jsonify(final_list)

    @jwt_required()
    def post(self, user_id):
        # TODO: check to see whether data was not repeated
        user_id = str(user_id)
        print ('help is ', (current_identity.__dict__))
        print ('Current object is', current_identity._get_current_object().id)
        if user_id != current_identity._get_current_object().id:
            abort(401)
        # data = request.form.get('job')
        # print ("data is ", data)
        args = self.reqparse.parse_args()
        job_inserted = args.get('job')
        print ('args are', args)
        # TODO: Enforce a already exists check, so that users use PUT to update instead
        # try:
        insertion_successful, job_id = self.jobsDAO.insert_job(user_id,
                                        job_inserted['make'], job_inserted['model'],
                                        job_inserted['year'], job_inserted['options'],
                                        job_inserted['summary'], job_inserted['description'])
        # print ("result )
        print ('Is the insertion succesful ? : ', insertion_successful)
        if insertion_successful:
            job = self.jobsDAO.find_job(user_id, job_id)
            print ('Job got in output is', job)
            return jsonify(results=job.__dict__)
        # give job id in return
        else:
            abort(401)

        # use put request to update the job description

    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        print ('Got user id is', user_id)
        if user_id != str(current_identity._get_current_object().id.encode('utf8'), encoding='utf-8'):
            print ('current_identity is curr', current_identity._get_current_object().id.encode('utf8'))
            print ('user id is', user_id)
            print ('Aborting because of current_identity issue')
            abort(401)

        args = self.reqparse.parse_args()
        print ('args are', args)

        job_to_update = args.get('job')
        print ('args are', args)
        job_id = args['job']['job_id']
        updated_values = {str(k): str(v) for k, v in args['job']['updated_values'].items()}
        update_successful = self.jobsDAO.update_job(user_id, job_id, updated_values)
        if update_successful:
            job = self.jobsDAO.find_job(user_id, job_id)
            # user = self.usersDAO.find_user(user_id)
            print ('Update was successful for', job.__dict__)
            return jsonify(results=job.__dict__)
        else:
            abort(404)



class JobDeletionAPI(Resource):

    def __init__(self):
        self.jobsDAO = JobRequestDAO(mongo)
        self.usersDAO = UsersDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('job', type=dict, location='json')

    @jwt_required()
    def delete(self, user_id, job_id):
        user_id = str(user_id)
        if user_id != str(current_identity._get_current_object().id.encode('utf8'), encoding='utf-8'):
            abort(401)

        delete_successful = self.jobsDAO.delete_one(user_id, job_id)
        if delete_successful:
            return {'result': True}
        else:
            abort(404)
