import boto3

BUCKET_NAME = "shotit"
COMPANY_EMAIL = "no-reply@shotit.com"
CORS_CONFIG = {
    "CORSRules": [
        {
            "AllowedOrigins": ["*"],
            "AllowedMethods": ["PUT"],
            "AllowedHeaders": ["Content-Type"],
            "MaxAgeSeconds": 3000,
        }
    ]
}

s3 = boto3.client("s3")
ses = boto3.client("ses")

def setup_simple_email_service():
    try:
        ses.verify_email_identity(EmailAddress = COMPANY_EMAIL)
    except Exception as error:
        print(f"[SES]: ${error.message}")

def setup_simple_storage_service():
    try:
        s3.create_bucket(Bucket = BUCKET_NAME)
        s3.delete_bucket_cors(Bucket = BUCKET_NAME)
        s3.put_bucket_cors(Bucket = BUCKET_NAME, CORSConfiguration = CORS_CONFIG)
    except Exception as error:
        print(f"[S3]: {error.message}")

if __name__ == "__main__":
    setup_simple_email_service()
    setup_simple_storage_service()