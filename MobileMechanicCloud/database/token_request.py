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
            if user:
                return token_model.Token(token_data['user_id'], token_data['token'])
            return None
        except:
            return None

    # TODO: rename the collection -> notifications
    def insert_token(self, user_id, token):
        try:
            result = self.db.userTokens.insert_one({'user_id': user_id, 'token': token})
            logging.debug('Token insert result is ' + str(result.inserted_id))
            return result.acknowledged
        except:
            return False