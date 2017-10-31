class JobRequest:
    def __init__(self, user_id, job_id, make, model, year, options,
                 summary, description, images):
        self.user_id = user_id
        self.job_id = job_id
        self.make = make
        self.model = model
        self.year = year
        self.options = options
        self.summary = summary
        self.description = description
        self.images = images

    def __str__(self):
        return "Job(user_id='{0}',job_id='{1}')".format(self.user_id, self.job_id)
