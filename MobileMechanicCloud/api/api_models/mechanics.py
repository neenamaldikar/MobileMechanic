from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify
from database.mechanic_request import MechanicDAO
from extensions import mongo

class MechanicAPI(Resource):

    def __init__(self):
        self.mechanicDAO = MechanicDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('updated_values', type=dict, location='json')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        mechanic = self.mechanicDAO.find_mechanic(user_id)
        if mechanic is None:
            abort(401)

        return jsonify(mechanic.__dict__)

    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        if not args.get('updated_values'):
            abort(401)
        new_values = {str(k): str(v) for k, v in args['updated_values'].items()}
        if any([i == 'user_id' for i in new_values]):
            abort(401)
        if any([i not in ['phone_number', 'address_line1', 'address_line2', 'city', 'state', 'zipcode', 'rate',
                          'rating', 'reviews'] for i in new_values]):
            abort(401)

        mechanic = self.mechanicDAO.find_mechanic(user_id)
        if mechanic is None:
            insertion_successful = self.mechanicDAO.insert_mechanic(user_id)
            if not insertion_successful:
                abort(401)
        update_successful = self.mechanicDAO.update_mechanic(user_id, new_values)
        if update_successful:
            mechanic = self.mechanicDAO.find_mechanic(user_id)
            return jsonify(results=mechanic.__dict__)
        else:
            abort(401)
