import time
from extensions import mongo
from model import mechanic_model
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

class MechanicDAO:
    def __init__(self, mongo):
        self.db = mongo.db

    def find_mechanic(self, user_id):
        try:
            mechanic = self.db.mechanics.find_one({'user_id': user_id})
            if mechanic is None:
                return None
            return mechanic_model.Mechanic(user_id=mechanic['user_id'], phone_number=mechanic.get('phone_number'),
                                   address_line=mechanic.get('address_line'),
                                   city=mechanic.get('city'), state=mechanic.get('state'),
                                   zipcode=mechanic.get('zipcode'), rate=mechanic.get('rate'),
                                   rating=mechanic.get('rating'), reviews=mechanic.get('reviews'))
        except:
            return None

    def insert_mechanic(self, user_id):
        try:
            result = self.db.mechanics.insert_one({'user_id': user_id})
            return result.acknowledged
        except:
            return False

    # this step is required for jobs
    def update_mechanic(self, user_id, updated_values):
        try:
            logging.debug('Updating user with' + str(updated_values))
            mechanic_reviews = updated_values['reviews']
            updated_values['reviews'] = []
            result = self.db.mechanics.update_one({'user_id': user_id}, {'$set': updated_values})
            for review in mechanic_reviews:
                result = self.db.mechanics.update_one({'user_id': user_id},
                                                 {'$push': {'reviews': review}})
            if result.matched_count == 1:
                return True
            return False
        except:
            return False
