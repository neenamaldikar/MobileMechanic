class User:
    def __init__(self, user_id, first_name, last_name,
                 email, last_seen, address_line1=None,
                 address_line2=None, city=None, state=None, zipcode=None, phone_number=None):
        # additional quickfix
        self.id = user_id  # used only by the current_identity module in jwt
        self.user_id = user_id
        self.first_name = first_name
        self.last_name = last_name
        self.email = email
        self.last_seen = last_seen
        self.address_line1 = address_line1
        self.address_line2 = address_line2
        self.city = city
        self.state = state
        self.zipcode = zipcode
        self.phone_number = phone_number

    def __str__(self):
        return "User(user_id='{0}')".format(self.user_id)
