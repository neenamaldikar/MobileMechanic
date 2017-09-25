from flask_restful import fields, Resource, reqparse, marshal, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify
from DAO.usersDAO import UsersDAO
from app import db

import collections

class UserAPI(Resource):

    def __init__(self):
        self.usersDAO = UsersDAO(db)
        self.reqparse = reqparse.RequestParser()
        #self.reqparse.add_argument('city', type = str, location = 'form')
        self.reqparse.add_argument('updated_values', type = dict, location = 'json')
        super(UserAPI, self).__init__()

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id.encode('utf8'):
            abort(401)

        user = self.usersDAO.find_user(user_id)
        if user is None:
            abort(404)

        return jsonify(user.__dict__)

    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id.encode('utf8'):
            abort(401)

        args = self.reqparse.parse_args()
        new_values = {str(k):(str(v) if isinstance(v, unicode) else v)
                      for k,v in args['updated_values'].items()}
        update_successful = self.usersDAO.update_user(user_id, new_values)
        if update_successful:
            user = self.usersDAO.find_user(user_id)
            return jsonify(results=user.__dict__)
        else:
            abort(404)


    @jwt_required()
    def delete(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id.encode('utf8'):
            abort(401)

        delete_successful = self.usersDAO.delete_one(user_id)
        if delete_successful:
            return {'result': True}
        else:
            abort(404)
    '''
    def delete(self, user_id):
        user = [user for user in users if user['user_id'] == user_id]
        if len(user) == 0:
            abort(404)
        users.remove(user[0])
        return {'result': True}
    '''

    '''
    def put(self, user_id):
        user = filter(lambda u: u['user_id'] == user_id, users)
        if len(user) == 0:
            abort(404)
        user = user[0]
        args = self.reqparse.parse_args()
        for k, v in args.iteritems():
            if v is not None:
                user[k] = v
            return { 'user': marshal(user, user_fields) }, 201
    '''
