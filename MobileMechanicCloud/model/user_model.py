class User(object):
    def __init__(self, id, first_name, last_name, email, last_seen,
                 address_line1=None, address_line2=None, city=None, state=None, zip=None):
        self.id = id #idp user_id
        self.first_name = first_name
        self.last_name = last_name
        self.email = email
        self.last_seen = last_seen
        self.address_line1 = address_line1
        self.address_line2 = address_line2
        self.city = city
        self.state = state
        self.zip = zip

    def __str__(self):
        return "User(id='%s')" % self.id
