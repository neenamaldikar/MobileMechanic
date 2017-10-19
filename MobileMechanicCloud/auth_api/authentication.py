import facebook
from database.database import UsersDAO
from extensions import mongo
import os
# check safety of app usage

def authenticate(username, password):
    usersDAO = UsersDAO(mongo)
    profile, token_app_id, token_application = get_info_from_token(username)
    if profile is None:
        print ('No profile')
        return
    if is_appinfo_valid(token_app_id, token_application) == False:
        print ('App info invalid')
        return

    user = usersDAO.find_user(profile.get('id'))
    print('id is', profile.get('id'))
    if user is None:
        print ('New user being inserted')
        usersDAO.insert_user(profile.get('id'), profile.get('first_name'),
                             profile.get('last_name'),profile.get('email'))
        user = usersDAO.find_user(profile.get('id'))

    return user


def is_appinfo_valid(token_app_id, token_application):
    if str(token_app_id.encode('utf8'), encoding='utf-8') == os.getenv('FB_APP_ID') and str(token_application.encode('utf8'), encoding='utf-8') == os.getenv('FB_APP_NAME'):
        return True
    else:
        print ('invalid because')
        print (token_app_id.encode('utf-8'), 'is actual app id and yours', os.getenv('FB_APP_ID'))
        print (token_application.encode('utf-8'), 'is actual app name and yours', os.getenv('FB_APP_NAME'))
        return False


def get_info_from_token(username):
    try:
        graph = facebook.GraphAPI(access_token=username)
    except facebook.GraphAPIError:
        print ('Graph error')
        return

    print (graph, 'is graph')
    print('debug token', username, 'app id', os.getenv('FB_APP_ID'), 'app_secret', os.getenv('FB_APP_SECRET'))
    token_info = graph.debug_access_token(token=username,
                                          app_id=os.getenv('FB_APP_ID'),
                                          app_secret=os.getenv('FB_APP_SECRET'))
    # unnecessary
    print ('token_info', token_info)
    # valid = token_info.get('data').get('is_valid')

    if token_info.get('data').get('is_valid'):
        print ('token info is valid')
        token_app_id = token_info.get('data').get('app_id')
        token_application = token_info.get('data').get('application')
        args = {'fields': 'id,first_name,last_name,email'}
        # profile=graph.get_object('me')
        profile = graph.get_object('me', **args)
    else:
        print ('token info invalid')
        profile = token_app_id = token_application = None

    return profile, token_app_id, token_application

def identity(payload):
    usersDAO = UsersDAO(mongo)
    current_user = usersDAO.find_user(payload['identity'])
    return current_user
