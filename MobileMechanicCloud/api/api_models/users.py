# TODO: Find an alternative to using the _get_current_object().id

from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify
from database.user_request import UserDAO
from extensions import mongo


class UserAPI(Resource):

    def __init__(self):
        self.userDAO = UserDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('updated_values', type=dict, location='json')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        user = self.userDAO.find_user(user_id)
        if not user:
            abort(404)
        return user.as_dict()

    # add validations to the PUT request before the data being sent in can be used to update values
    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        if not args.get('updated_values'):
            abort(400)
        #new_values = {str(k): str(v) for k, v in args['updated_values'].items()}
        new_values = args.get('updated_values')
        if any([i in new_values for i in ['user_id', 'email_address', 'first_name', 'last_name']]):
            abort(405)
        if any([i not in ['phone_number', 'address_line', 'city', 'state', 'zipcode'] for i in new_values]):
            abort(400)
        update_successful = self.userDAO.update_user(user_id, new_values)
        if not update_successful:
            abort(401)
        user = self.userDAO.find_user(user_id)
        if not user:
            abort(404)
        return user.as_dict()

    @jwt_required()
    def delete(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        delete_successful = self.userDAO.delete_one(user_id)
        if not delete_successful:
            abort(404)
        return {'success': "User has been deleted successfully."}
