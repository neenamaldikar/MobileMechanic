class Mechanic:
    def __init__(self, user_id, phone_number=None, address_line=None,
                 city=None, state=None, zipcode=None, rate=None,
                 rating=None, reviews=None):
        self.user_id = user_id
        self.phone_number = phone_number
        self.address_line = address_line
        self.city = city
        self.state = state
        self.zipcode = zipcode
        self.rate = rate
        self.rating = rating
        self.reviews = reviews

    def __str__(self):
        return "Mechanic(user_id='{0}')".format(self.user_id)

    def as_dict(self):
        return {
            'user_id': self.user_id,
            'phone_number': self.phone_number,
            'address_line': self.address_line,
            'city': self.city,
            'state': self.state,
            'zipcode': self.zipcode,
            'rate': self.rate,
            'rating': self.rating,
            'reviews': self.reviews
        }
