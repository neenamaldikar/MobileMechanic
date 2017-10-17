from flask import Flask
from flask_pymongo import PyMongo
# to avoid circular dependencies in PyMongo, JWT and flask_restful
from flask_jwt import JWT
from extensions import mongo, api
from auth_api.api_models.users import UserAPI
from auth_api.api_models.upload import ImageUploadAPI
from auth_api.authentication import authenticate, identity

def initialize_app():
    app = Flask(__name__)
    app.config.from_object('configuration.DevelopmentConfig')
    # config_prefix is used incase we want to add more databases
    # does this automatically change the mongo object in the original app?
    mongo.init_app(app, config_prefix='MONGO')
    jwt = JWT(app, authenticate, identity)
    api_base_string = '/mobilemechanic/api/v1.0/users/<int:user_id>'
    api.add_resource(UserAPI, api_base_string, endpoint='user')
    api.add_resource(ImageUploadAPI,
                     api_base_string + '/job/<int:job_id>/picture')
    api.init_app(app)
    return app

if __name__ == '__main__':
    app = initialize_app()
    app.run()
