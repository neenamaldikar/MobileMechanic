from flask_restful import Resource, reqparse, abort
from flask_jwt import jwt_required, current_identity
from flask import jsonify, request
from database.token_request import TokenDAO
from extensions import mongo
from model.token_model import Token
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

class TokenAPI(Resource):

    def __init__(self):
        self.tokenDAO = TokenDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('tokenData', type=dict, location='json')
        logging.debug('Call to TokenAPI made on server ...')

    @jwt_required()
    def get(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)
        
        token_data = self.tokenDAO.find_token(user_id)
        if token_data:
            return token_data.as_dict()
        abort(401)

    # TODO: remove the user_id; the user_id field is redundant
    @jwt_required()
    def post(self, user_id):
        user_id = str(user_id)
        if user_id != current_identity._get_current_object().id:
            abort(401)
        
        args = self.reqparse.parse_args()
        logging.debug('Current args are ... ' + str(args))
        token_inserted = args.get('tokenData')
        # if there is a problem with the inserted job format
        if not token_inserted:
            logging.debug('"tokenData" key missing in inserted token.')
            abort(401)
        logging.debug("Token inserted is " + str(token_inserted))
        if not len([i for i in token_inserted.keys() if i in ['user_id', 'fcmtoken']]) == 2:
            logging.debug('tokenData missing a few parameters ...')
            abort(401)
        if not self.tokenDAO.find_user(user_id):
            logging.debug('User not found, adding new token')
            insertion_successful = self.tokenDAO.insert_token(user_id, token_inserted['fcmtoken'])
            if not insertion_successful:
                abort(401)
            return Token(user_id, token_inserted['fcmtoken']).as_dict()
        else:
            # update token for existing user
            logging.debug('Updating token for existing user : ' + str(user_id))
            update_successful = self.tokenDAO.update_token(user_id, token_inserted['fcmtoken'])
            if not update_successful:
                abort(401)
            return Token(user_id, token_inserted['fcmtoken']).as_dict()
        abort(405)