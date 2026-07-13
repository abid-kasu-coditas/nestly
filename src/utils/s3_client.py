import boto3
import uuid
from src.core.config import settings



BUCKET_NAME = settings.AWS_BUCKET_NAME
s3_client = boto3.client("s3",aws_access_key_id=settings.AWS_ACCESS_KEY_ID,aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,region_name=settings.AWS_DEFAULT_REGION)



# def generate_s3_key(filename: str) -> str:
#     ext = filename.rsplit(".", 1)[-1].lower() if "." in filename else "bin"
#     return f"Property_photo/{uuid.uuid4()}.{ext}"
    

    



