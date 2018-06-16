import time
import bson
import uuid
from gridfs import GridFS
from extensions import mongo
from model import job_model
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)


class JobRequestDAO:
    def __init__(self, mongo):
        self.db = mongo.db
        self.fs = GridFS(self.db)

    def find_job(self, user_id=None, job_id=None, zipcodes=None):
        try:
            if job_id:
                logging.debug('Finding jobs listed for user' + user_id)
                cursor = self.db.jobs.find_one({'user_id': user_id, 'job_id': job_id})
                # should there be a check for each option?
                if not cursor:
                    logging.debug('No cursor data')
                    return None
                else:
                    return job_model.JobRequest(user_id, job_id, cursor['make'], cursor['model'], cursor['year'],
                                                cursor['options'], cursor['summary'], cursor['description'],
                                                cursor['images'], cursor['status'], cursor['address_line'],
                                                cursor['city'], cursor['state'], cursor['zipcode'])
            if user_id:
                cursor = self.db.jobs.find({'user_id': user_id})

            if zipcodes:
                cursor = self.db.jobs.find({"zipcode": {"$in": zipcodes}})

            if not cursor:
                logging.debug('No cursor data')

            data = list(cursor)
            if not data:
                logging.debug("Job list is empty")

            output_list = []
            for i in data:
                output_list.append(job_model.JobRequest(i['user_id'], i['job_id'], i['make'], i['model'], i['year'],
                                                        i['options'], i['summary'], i['description'],
                                                        i['images'], i['status'], i['address_line'], i['city'],
                                                        i['state'], i['zipcode']))
            return output_list
        except:
            logging.debug('Exception in find request')
            return None

    # TODO: make the job insertion unique here
    # TODO: add validations for the options passed in the options field
    def insert_job(self, user_id, make, model, year, options, summary, description,
                   status, address_line, city, state, zipcode):
        try:
            unique_job_id = str(uuid.uuid4())
            while True:
                cursor = self.db.jobs.find_one({'user_id': user_id, 'job_id': unique_job_id})
                if cursor:
                    unique_job_id = str(uuid.uuid4())
                else:
                    break
            result = self.db.jobs.insert_one({'user_id': user_id, 'job_id': unique_job_id,
                                              'make': make, 'model': model, 'year': year,
                                              'options': options, 'summary': summary,
                                              'description': description, 'images': [],
                                              'status': status, 'address_line': address_line,
                                              'city': city, 'state': state, 'zipcode': zipcode,
                                              'number_of_quotes':0})
            return (result.acknowledged, unique_job_id)
        except:
            return False

    def update_job(self, user_id, job_id, updated_values):
        try:
            result = self.db.jobs.update_one({'user_id': user_id, 'job_id': job_id},
                                             {'$set': updated_values})
            if result.matched_count == 1:
                return True
            else:
                return False
        except:
            return False

    def update_images(self, user_id, job_id, image_data, filename, content_type):
        try:
            image_id = self.fs.put(image_data, content_type=content_type, filename=filename)
            logging.info('Pushing images to database')
            result = self.db.jobs.update_one({'user_id': user_id, 'job_id': job_id},
                                             {'$push': {'images': str(image_id)}})
            if result.matched_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False

    def get_image(self, user_id, job_id, image_id):
        try:
            image_ptr = self.fs.get(bson.objectid.ObjectId(image_id))
            image_data = image_ptr.read()
            image_ptr.close()
            return image_data
        except:
            return None

    def delete_one(self, user_id, job_id):
        try:
            result = self.db.jobs.delete_one({'user_id': user_id, 'job_id': job_id})
            if result.deleted_count == 1:
                return True
            else:
                return False
        except:
            return False
