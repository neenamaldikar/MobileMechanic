import uuid
from gridfs import GridFS
from extensions import mongo
from model import quote_model
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

class QuoteRequestDAO:
    def __init__(self, mongo):
        self.db = mongo.db
        self.fs = GridFS(self.db)

    def find_quote(self, quote_id,job_id, mechanic_id=None):
        try:
            logging.debug('Finding quote: ' + quote_id + 'for job: ' + job_id)
            if mechanic_id:
                cursor = self.db.quotes.find_one({'quote_id': quote_id, 'job_id': job_id,
                                                  'mechanic_user_id':mechanic_id})
            else:
                cursor = self.db.quotes.find_one({'quote_id': quote_id, 'job_id': job_id})
            if not cursor:
                logging.debug('No cursor data')
                return None
            else:
                return quote_model.QuoteRequest(
                    cursor['job_id'], cursor['customer_user_id'], cursor['mechanic_user_id'],
                    quote_id, cursor['labor_cost'], cursor['part_cost'],
                    cursor['onsite_service_charges'], cursor['comments'], cursor['status']
                )
        except:
            logging.debug('Exception in finding quote: ' + quote_id + ' for job: ' + job_id)
            return None

    def find_quotes(self, user_id ,job_id, mechanic_id=None):
        try:
            logging.debug('Finding quote for user: ' + user_id + 'and job: ' + job_id)
            if mechanic_id:
                cursor = self.db.quotes.find({'customer_user_id': user_id, 'job_id': job_id,
                                              'mechanic_user_id' : mechanic_id})
            else:
                cursor = self.db.quotes.find({'customer_user_id': user_id, 'job_id': job_id})
            if not cursor:
                logging.debug('No cursor data for find_quotes')

            data = list(cursor)
            if not data:
                logging.debug("Quotes list is empty")

            output_list = []
            for i in data:
                output_list.append(quote_model.QuoteRequest(
                    i['job_id'], i['customer_user_id'], i['mechanic_user_id'], i['quote_id'],
                    i['labor_cost'], i['part_cost'], i['onsite_service_charges'],
                    i['comments'], i['status']
                ))
            return output_list
        except:
            logging.debug('Exception in finding job: ' + job_id + ' for user: ' + user_id)
            return None

    def insert_quote(self, job_id, customer_user_id, mechanic_user_id, labor_cost, part_cost,
                     onsite_service_charges, comments, status):
        try:
            unique_quote_id = str(uuid.uuid4())
            while True:
                cursor = self.db.jobs.find_one({'quote_id': unique_quote_id, 'job_id': job_id})
                if cursor:
                    unique_quote_id = str(uuid.uuid4())
                else:
                    break
            result = self.db.quotes.insert_one(
                {'job_id': job_id, 'customer_user_id': customer_user_id,
                 'mechanic_user_id': mechanic_user_id, 'quote_id': unique_quote_id, 'labor_cost': labor_cost,
                 'part_cost': part_cost, 'onsite_service_charges': onsite_service_charges,
                 'comments': comments, 'status': status}
            )
            return (result.acknowledged, unique_quote_id)
        except:
            return False

    def update_quote(self, quote_id, job_id, updated_values):
        try:
            result = self.db.quotes.update_one({'quote_id': quote_id, 'job_id': job_id},
                                             {'$set': updated_values})
            if result.matched_count == 1:
                return True
            else:
                return False
        except:
            return False

    def delete_quotes(self, job_id, mechanic_user_id):
        try:
            result = self.db.quotes.delete_many({'mechanic_user_id': mechanic_user_id, 'job_id': job_id})
            if result.deleted_count >= 1:
                return True
            else:
                return False
        except:
            return False

    def delete_one(self, quote_id, job_id, mechanic_user_id):
        try:
            result = self.db.quotes.delete_one({'quote_id': quote_id, 'job_id': job_id,
                                                'mechanic_user_id': mechanic_user_id})
            if result.deleted_count == 1:
                return True
            else:
                return False
        except:
            return False

    def getNumberOfQuotes(self, job_id, user_id):
        try:
            result_count = self.db.quotes.count({"job_id":job_id,"customer_user_id":user_id})
            return result_count
        except:
            return 0
