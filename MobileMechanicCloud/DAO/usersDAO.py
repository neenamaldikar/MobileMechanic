from DTO.user import User
import time
from pymongo import errors
# The UserProfile Data Access Object handles all interactions with the UserProfile table.


class UsersDAO:
    def __init__(self, db):
        self.db = db

    def find_user(self, user_id):
        try:
            user = self.db.users.find_one({'id': user_id})
        except errors.OperationFailure:
            user = None

        if user is None:
            return None
        else:
            return User(user['id'],user['first_name'], user['last_name'],
                        user['email'], user['last_seen'],state=user.get('state'),
                        zip=user.get('zip'), city=user.get('city'),
                        address_line1=user.get('address_line1'),address_line2=user.get('address_line2'))

    def insert_user(self, user_id, first_name, last_name, email):
        try:
            result = self.db.users.insert_one({'id': user_id, 'first_name': first_name,
                                               'last_name': last_name, 'email': email,
                                               'last_seen': time.time()})
            return result.acknowledged
        except errors.PyMongoError:
            return False

    def update_user(self, user_id, updated_values):
        try:
            #updated_values = str(updated_values)
            result = self.db.users.update_one({'id': user_id}, {'$set': updated_values})
            if result.matched_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False

    def delete_one(self, user_id):
        try:
            result = self.db.users.delete_one({'id': user_id})
            if result.deleted_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False
