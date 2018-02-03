# TODO: add a few more validations to the fields

class JobRequest:
    def __init__(self, user_id, job_id, make, model, year, options,
                 summary, description, images, status, address_line,
                 city, state, zipcode):
        self.user_id = user_id
        self.job_id = job_id
        self.make = make
        self.model = model
        self.year = year
        self.options = options
        self.summary = summary
        self.description = description
        self.images = images
        self.status = status
        self.address_line=address_line
        self.city=city
        self.state=state
        self.zipcode=zipcode

    def __str__(self):
        return "Job(user_id='{0}', job_id='{1}')".format(self.user_id, self.job_id)

    def as_dict(self):
        return {
            'user_id': self.user_id,
            'job_id': self.job_id,
            'make': self.make,
            'model': self.model,
            'year': self.year,
            'options': self.options,
            'summary': self.summary,
            'description': self.description,
            'images': self.images,
            'status': self.status,
            'address_line': self.address_line,
            'city': self.city,
            'state': self.state,
            'zipcode': self.zipcode
        }