import os
import datetime
import sys
import logging.config

# if we work with multiple databases, we'll need to use the
# configuration prefix for each of the databases
LOGGING_JSON = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'standard': {
            'format': '%(asctime)s - %(name)s - %(levelname)s: %(message)s'
        },
    },
    'handlers': {
        'console': {
            'level':'DEBUG',
            'class':'logging.StreamHandler',
            'stream': sys.stderr,
            'formatter': 'standard'
        }
    },
    'loggers': {
        '': {
            'handlers': ['console'],
            'level': 'INFO'
        }
    }
}

# test the loggers over here
logging.config.dictConfig(LOGGING_JSON)
logging.debug('Test debug log from configuration module')
logging.info('Test info log from configuration module')

class DevelopmentConfig:
    DEBUG = True
    FB_APP_ID = os.getenv('FB_APP_ID', '')
    FB_APP_SECRET = os.getenv('FB_APP_SECRET', '')
    FB_APP_NAME = os.getenv('FB_APP_NAME', '')
    SECRET_KEY = os.getenv('APP_SECRET_KEY', '')
    JWT_AUTH_URL_RULE = os.getenv('JWT_AUTH_RULE', '')
    JWT_EXPIRATION_DELTA = datetime.timedelta(minutes=60)
    MONGO_URI = os.getenv('MONGO_URI', '')
    LOGGING_JSON = LOGGING_JSON

class ProductionConfig:
    DEBUG = False
    FB_APP_ID = os.getenv('FB_APP_ID', '')
    FB_APP_SECRET = os.getenv('FB_APP_SECRET', '')
    FB_APP_NAME = os.getenv('FB_APP_NAME', '')
    SECRET_KEY = os.getenv('APP_SECRET_KEY', '')
    JWT_AUTH_URL_RULE = os.getenv('JWT_AUTH_RULE', '')
    JWT_EXPIRATION_DELTA = datetime.timedelta(minutes=15)
    MONGO_URI = os.getenv('MONGO_URI', '')
    LOGGING_JSON = LOGGING_JSON