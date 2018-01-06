import sys
import os
import logging
from flask import Flask
from flask_pymongo import PyMongo
from waitress import serve
# to avoid circular dependencies in PyMongo, JWT and flask_restful
from flask_jwt import JWT
from extensions import mongo, api
from configuration import LOGGING_JSON
from api.api_models.users import UserAPI
from api.api_models.jobs import JobAPI
from api.api_models.upload import ImageUploadAPI
from api.api_models.mechanics import MechanicAPI
from api.authentication import authenticate, identity

def initialize_app():
    app = Flask(__name__)
    app.config.from_object('configuration.DevelopmentConfig')
    # config_prefix is used incase we want to add more databases
    mongo.init_app(app, config_prefix='MONGO')
    jwt = JWT(app, authenticate, identity)
    api_base_string = '/mobilemechanic/api/v1.0/'
    api.add_resource(UserAPI, api_base_string + 'users/<int:user_id>')
    api.add_resource(JobAPI, api_base_string + 'users/<int:user_id>/jobs')
    api.add_resource(ImageUploadAPI,
                     api_base_string + 'users/<int:user_id>/jobs/<job_id>/picture')
    api.add_resource(MechanicAPI, api_base_string + 'users/<int:user_id>/mechanic')
    api.init_app(app)
    return app

if __name__ == '__main__':
    app = initialize_app()
    logging.config.dictConfig(LOGGING_JSON)
    logging.info('Serving on port - ' + os.environ.get('PORT'))
    logging.debug('Mongo URI used is ' + app.config.get('MONGO_URI'))
    serve(app, port=os.environ.get('PORT', 5000), cleanup_interval=100)
