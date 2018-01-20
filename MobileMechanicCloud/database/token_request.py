from extensions import mongo
from model import token_model
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

class TokenDAO:
    def __init__(self, mongo):
        self.db = mongo.db

    def find_user_token(self, user_id):
        try:
            token_data = self.db.userTokens.find_one({'user_id': user_id})
            if token_data:
                return token_model.Token(token_data['user_id'], token_data['token'])
            return None
        except:
            return None

    # TODO: rename the collection -> notifications
    def insert_token(self, user_id, token):
        try:
            if not self.find_user_token(user_id):
                result = self.db.userTokens.insert_one({'user_id': user_id, 'token': token})
                logging.debug('Token insert result is ' + str(result.inserted_id))
                return result.acknowledged
            return False
        except:
            return False

    def find_user(self, user_id):
        try:
            token_data = self.db.userTokens.find_one({'user_id': user_id})
            logging.debug('User found in userTokens ...')
            if token_data:
                return True
            return False
        except:
            return False

    def update_token(self, user_id, token):
        try:
            result = self.db.userTokens.update_one({'user_id': user_id}, {'$set': {'token': token}})
            return result.acknowledged
        except Exception as ex:
            return False
