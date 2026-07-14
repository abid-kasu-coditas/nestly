import boto3
from src.core.config import settings


BUCKET_NAME = settings.AWS_BUCKET_NAME
s3_client = boto3.client("s3",aws_access_key_id=settings.AWS_ACCESS_KEY_ID,aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,region_name=settings.AWS_DEFAULT_REGION)



def upload_file_to_s3(doc: str , folder : str) -> str:
    
    #upload the file and return the url 
    pass










