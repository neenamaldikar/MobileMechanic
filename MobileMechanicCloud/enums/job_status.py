from enum import Enum


class JobStatus(Enum):
    submitted = 1
    quotes_available = 2
    quote_accepted = 3
    job_in_progress = 4
    job_completed = 5
    job_request_cancelled = 6
