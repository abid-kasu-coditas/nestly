import boto3
import uuid
from fastapi import UploadFile
from src.core.config import settings

s3_client = boto3.client(
    "s3",
    aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
    aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
    region_name=settings.AWS_DEFAULT_REGION,
)

def upload_file_to_s3(file: UploadFile, folder: str) -> str:
    ext = file.filename.rsplit(".", 1)[-1].lower()
    unique_name = f"{folder}/{uuid.uuid4()}.{ext}"

    s3_client.upload_fileobj(
        file.file,
        settings.AWS_BUCKET_NAME,
        unique_name,
    )

    return unique_name   

def generate_presigned_url(key: str, expires_in: int = 3600) -> str:

    return s3_client.generate_presigned_url(
        "get_object",
        Params={"Bucket": settings.AWS_BUCKET_NAME, "Key": key},
        ExpiresIn=expires_in,  
    )
