import time
from extensions import mongo
from model import user_model
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

class UserDAO:
    def __init__(self, mongo):
        self.db = mongo.db

    def find_user(self, user_id):
        try:
            user = self.db.users.find_one({'user_id': user_id})
            if user:
                return user_model.User(user['user_id'], user['first_name'],
                                       user['last_name'], user['email'], user['gender'],
                                       user['last_seen'], address_line=user.get('address_line'),
                                       city=user.get('city'), state=user.get('state'),
                                       zipcode=user.get('zipcode'), phone_number=user.get('phone_number'))
            return None
        except:
            return None

    def insert_user(self, user_id, first_name, last_name, email, gender):
        try:
            result = self.db.users.insert_one({'user_id': user_id, 'first_name': first_name,
                                               'last_name': last_name, 'email': email,
                                               'gender': gender, 'last_seen': time.time()})
            logging.debug('User insert result is ' + str(result.inserted_id))
            return result.acknowledged
        except:
            return False

    # this step is required for jobs
    def update_user(self, user_id, updated_values):
        try:
            logging.debug('Updating user with' + str(updated_values))
            result = self.db.users.update_one({'user_id': user_id}, {'$set': updated_values})
            if result.matched_count == 1:
                return True
            return False
        except:
            return False

    def delete_one(self, user_id):
        try:
            result = self.db.users.delete_one({'user_id': user_id})
            if result.deleted_count == 1:
                return True
            return False
        except:
            return False
