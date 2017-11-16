from extensions import mongo
from model import mechanic_model
from pymongo import errors
import time

class MechanicDAO:
    def __init__(self, mongo):
        self.db = mongo.db

    def find_mechanic(self, user_id):
        try:
            mechanic = self.db.mechanics.find_one({'user_id': user_id})
            if mechanic is None:
                return None
            return mechanic_model.Mechanic(user_id=mechanic['user_id'], phone_number=mechanic.get('phone_number'),
                                   address_line1=mechanic.get('address_line1'),
                                   address_line2=mechanic.get('address_line2'),
                                   city=mechanic.get('city'), state=mechanic.get('state'),
                                   zipcode=mechanic.get('zipcode'), rate=mechanic.get('rate'),
                                   rating=mechanic.get('rating'), reviews=mechanic.get('reviews'))
        except errors.OperationFailure:
            return None

    def insert_mechanic(self, user_id):
        try:
            result = self.db.mechanics.insert_one({'user_id': user_id})
            return result.acknowledged
        except errors.PyMongoError:
            return False

    # this step is required for jobs
    def update_mechanic(self, user_id, updated_values):
        try:
            print ('Updating user with', updated_values)
            result = self.db.mechanics.update_one({'user_id': user_id}, {'$set': updated_values})
            if result.matched_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False
