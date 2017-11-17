import os
import facebook
from extensions import mongo
from database.user_request import UserDAO
from flask import current_app
from configuration import LOGGING_JSON
import logging.config
logging.config.dictConfig(LOGGING_JSON)

def authenticate(username, password):
    userDAO = UserDAO(mongo)
    profile, token_app_id, token_application = get_info_from_token(username)
    if profile and is_appinfo_valid(token_app_id, token_application):
        user = userDAO.find_user(profile.get('id'))
        logging.info('Current user id is ' + profile.get('id'))
        if not user:
            logging.info('User does not exist. Creating user ...')
            logging.debug('User profile is ' + str(profile))
            userDAO.insert_user(profile.get('id'), profile.get('first_name'), profile.get('last_name'),
                                profile.get('email'), profile.get('gender'))
            user = userDAO.find_user(profile.get('id'))
            logging.debug('Fetched profile is ' + str(user))
        return user


def is_appinfo_valid(token_app_id, token_application):
    logging.debug('Token is ' + token_app_id)
    logging.debug('Token application is ' + token_application)
    if (token_app_id == current_app.config.get('FB_APP_ID')) and (token_application == current_app.config.get('FB_APP_NAME')):
        return True
    return False


def get_info_from_token(username):
    try:
        graph = facebook.GraphAPI(access_token=username)
        token_info = graph.debug_access_token(token=username,
                                              app_id=current_app.config.get('FB_APP_ID'),
                                              app_secret=current_app.config.get('FB_APP_SECRET'))

        if token_info.get('data').get('is_valid'):
            token_app_id = token_info.get('data').get('app_id')
            token_application = token_info.get('data').get('application')
            args = {'fields': 'id,first_name,last_name,email,gender'}
            profile = graph.get_object('me', **args)
            logging.debug('Fetched profile is ' + str(profile))
        else:
            profile = token_app_id = token_application = None

        return profile, token_app_id, token_application
    except facebook.GraphAPIError:
        return


def identity(payload):
    userDAO = UserDAO(mongo)
    logging.debug('Identity function being called with payload ' + str(payload['identity']))
    current_user = userDAO.find_user(payload['identity'])
    logging.debug('Current user is ' + str(current_user))
    return current_user
