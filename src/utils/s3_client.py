import boto3
from src.core.config import settings
from fastapi import HTTPException
import uuid
import logging
from botocore.exceptions import ClientError


BUCKET_NAME = settings.AWS_BUCKET_NAME
s3_client = boto3.client("s3",aws_access_key_id=settings.AWS_ACCESS_KEY_ID,aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,region_name=settings.AWS_DEFAULT_REGION)



def upload_file_to_s3(doc: str , folder : str) -> str:
    
    #upload the file and return the url 
    
    ext = doc.filename.rsplit(".", 1)[-1].lower()
    
    
    if ext not in {"pdf", "jpg", "png", "docx"}:
            raise HTTPException(status_code=400, detail="Invalid file type. Allowed: pdf, jpg, png, docx")
    
    key = f"{doc.filename}/{uuid.uuid4()}"
    
    try:
        
        response = s3_client.file_upload(doc,BUCKET_NAME,key)
        
    except ClientError as e:
        logging.error(e)
        return False
    
    return key

    
    












