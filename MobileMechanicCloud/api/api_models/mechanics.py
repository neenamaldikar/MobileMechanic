from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify
from database.mechanic_request import MechanicDAO
from extensions import mongo
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

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
            abort(404, description="The mechanic you are searching for probably doesn't exist")
        return mechanic.as_dict()

    @jwt_required()
    def put(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)

        args = self.reqparse.parse_args()
        if not args.get('updated_values'):
            logging.debug('Cannot find updated values.')
            abort(400, description='The request is missing a few fields. Please check your request body once again.')
        #new_values = {str(k): (str(v) if type(v) == str else v) for k, v in args['updated_values'].items()}
        new_values = args.get('updated_values')
        if any([i == 'user_id' for i in new_values]):
            logging.debug('"user_id" not provided by mechanic.')
            abort(405, description='The request was sent in without a "user_id" field.')
        if not all([(len(i) == 5 and i.isdigit()) for i in new_values['serving_zipcodes']]):
            logging.debug('"serving_zipcodes" may not have properly formatted zipcodes inserted.')
            abort(400, description='The request was sent in with invalid zipcodes in the "serving_zipcodes" field.')
        if any([i not in ['phone_number', 'address_line', 'city', 'state', 'zipcode', 'rate',
                          'rating', 'reviews', 'serving_zipcodes'] for i in new_values]):
            logging.debug('Mechanic has not added a few fields.')
            abort(400, description='The request is missing a few fields. Please check your request body once again.')

        mechanic = self.mechanicDAO.find_mechanic(user_id)
        if mechanic is None:
            insertion_successful = self.mechanicDAO.insert_mechanic(user_id)
            if not insertion_successful:
                logging.debug('Could not insert mechanic data')
                abort(401)
        update_successful = self.mechanicDAO.update_mechanic(user_id, new_values)
        if not update_successful:
            abort(401)
        mechanic = self.mechanicDAO.find_mechanic(user_id)
        if not mechanic:
            abort(401)
        return mechanic.as_dict()