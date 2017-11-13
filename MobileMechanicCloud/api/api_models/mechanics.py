from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify
from database.user_request import UsersDAO
from extensions import mongo

class UserAPI(Resource):

    def __init__(self):
        self.usersDAO = UsersDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('updated_values', type=dict, location='json')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        user = self.usersDAO.find_user(user_id)
        if user is None:
            abort(404)

        return jsonify(user.__dict__)

    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        if not args.get('updated_values'):
            abort(401)
        new_values = {str(k): str(v) for k, v in args['updated_values'].items()}
        if any([i in new_values for i in ['user_id', 'email_address', 'first_name', 'last_name']]):
            abort(401)
        if any([i not in ['phone_number', 'address_line1', 'address_line2', 'city', 'state', 'zipcode'] for i in new_values]):
            abort(401)
        update_successful = self.usersDAO.update_user(user_id, new_values)
        if update_successful:
            user = self.usersDAO.find_user(user_id)
            return jsonify(results=user.__dict__)
        else:
            abort(401)

    @jwt_required()
    def delete(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        delete_successful = self.usersDAO.delete_one(user_id)
        if delete_successful:
            return {'success': "Deleted successfully!"}
        else:
            abort(404)
