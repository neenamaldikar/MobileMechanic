from extensions import mongo
from model import user_model
from pymongo import errors
import time

class UsersDAO:
    def __init__(self, mongo):
        self.db = mongo.db

    def find_user(self, user_id):
        try:
            user = self.db.users.find_one({'user_id': user_id})
            if user is None:
                return None
            return user_model.User(user['user_id'], user['first_name'],
                                   user['last_name'], user['email'], user['last_seen'],
                                   address_line1=user.get('address_line1'),
                                   address_line2=user.get('address_line2'),
                                   city=user.get('city'), state=user.get('state'),
                                   zipcode=user.get('zipcode'), phone_number=user.get('phone_number'))
        except errors.OperationFailure:
            return None

    def insert_user(self, user_id, first_name, last_name, email):
        try:
            result = self.db.users.insert_one({'user_id': user_id, 'first_name': first_name,
                                               'last_name': last_name, 'email': email,
                                               'last_seen': time.time()})
            return result.acknowledged
        except errors.PyMongoError:
            return False

    # this step is required for jobs
    def update_user(self, user_id, updated_values):
        try:
            print ('Updating user with', updated_values)
            result = self.db.users.update_one({'user_id': user_id}, {'$set': updated_values})
            if result.matched_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False

    def delete_one(self, user_id):
        try:
            result = self.db.users.delete_one({'user_id': user_id})
            if result.deleted_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False
