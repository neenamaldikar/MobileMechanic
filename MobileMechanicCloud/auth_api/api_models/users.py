from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify
from database.user_request import UsersDAO
from extensions import mongo

class UserAPI(Resource):

    def __init__(self):
        self.usersDAO = UsersDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        #self.reqparse.add_argument('city', type = str, location = 'form')
        self.reqparse.add_argument('updated_values', type=dict, location='json')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != str(current_identity._get_current_object().id.encode('utf8'), encoding='utf-8'):
            abort(401)

        user = self.usersDAO.find_user(user_id)
        if user is None:
            abort(404)

        return jsonify(user.__dict__)

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
        print ('items are', args['updated_values'])
        #new_values = {str(k): (str(v) if isinstance(v, unicode) else v)
        #              for k, v in args['updated_values'].items()}
        new_values = {str(k): str(v) for k, v in args['updated_values'].items()}
        update_successful = self.usersDAO.update_user(user_id, new_values)
        if update_successful:
            user = self.usersDAO.find_user(user_id)
            print ('Update was successful for', user.__dict__)
            return jsonify(results=user.__dict__)
        else:
            abort(404)

    @jwt_required()
    def delete(self, user_id):
        user_id = str(user_id)
        if user_id != str(current_identity._get_current_object().id.encode('utf8'), encoding='utf-8'):
            abort(401)

        delete_successful = self.usersDAO.delete_one(user_id)
        if delete_successful:
            return {'result': True}
        else:
            abort(404)
