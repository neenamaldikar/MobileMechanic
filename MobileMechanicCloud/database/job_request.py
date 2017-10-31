from extensions import mongo
from model import job_model
from pymongo import errors
import time
from gridfs import GridFS

class JobRequestDAO:
    def __init__(self, mongo):
        self.db = mongo.db

    def find_job(self, user_id, job_id=None):
        try:
            if job_id:
                print ('Trying for a job find with user', user_id)
                # input('Tried for a quick wait')
                cursor = self.db.jobs.find_one({'user_id': user_id, 'job_id': job_id})
                # should there be a check for each option?
                if not cursor:
                    print ('No cursor data')
                    return None
                else:
                    return job_model.JobRequest(user_id, job_id, cursor['make'], cursor['model'], cursor['year'], cursor['options'], cursor['summary'], cursor['description'], cursor['images'])
            else:
                cursor = self.db.jobs.find({'user_id': user_id})
                data = list(cursor)
                print('Cursor data is', data)
                if not cursor:
                    print('No cursor data')
                else:
                    output_list = []
                    for i in data:
                        output_list.append(job_model.JobRequest(user_id, i['job_id'],
                                        i['make'], i['model'], i['year'], i['options'],
                                        i['summary'], i['description'],
                                        i['images']))
                    return output_list
        except errors.OperationFailure:
            return None

    # generate a job id
    def insert_job(self, user_id, make, model, year, options, summary, description):
        try:
            unique_job_id = 'job_' + str(time.time())
            result = self.db.jobs.insert_one({'user_id': user_id, 'job_id': unique_job_id,
                                              'make': make, 'model': model, 'year': year,
                                              'options': options, 'summary': summary,
                                              'description': description, 'images': []})
            return (result.acknowledged, unique_job_id)
            # return result.acknowledged
        except errors.PyMongoError:
            return False

    def update_job(self, user_id, job_id, updated_values):
        try:
            result = self.db.jobs.update_one({'user_id': user_id, 'job_id': job_id},
                                             {'$set': updated_values})
            if result.matched_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False

    def update_images(self, user_id, job_id, image_data, filename, content_type):
        try:
            fs = GridFS(self.db)
            image_id = fs.put(image_data, content_type=content_type, filename=filename)
            print("Pushing images to database")
            result = self.db.jobs.update_one({"user_id": user_id, "job_id": job_id},
                                             {"$push": {"images": image_id}})
            if result.matched_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False

    def delete_one(self, user_id, job_id):
        try:
            result = self.db.users.delete_one({'user_id': user_id, 'job_id': job_id})
            if result.deleted_count == 1:
                return True
            else:
                return False
        except errors.PyMongoError:
            return False
