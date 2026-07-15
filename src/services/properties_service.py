from sqlalchemy.orm import Session
from fastapi import UploadFile, BackgroundTasks
from src.repositories import properties_repo
from src.models.documents import MediaType
from src.utils.s3_client import upload_file_to_s3
from src.services.verification_service import run_verification_panel_task_sync

def create_listing(db: Session,owner_id, title: str, description: str, location: str, rent_amount: int, amenities: str,
    photos: list[UploadFile],
    ownership_docs: list[UploadFile],
    background_tasks: BackgroundTasks):
    
    db_property = properties_repo.create_property(db, owner_id, title, description, location, rent_amount, amenities)

    for photo in photos:
        url = upload_file_to_s3(photo, folder=f"properties/{db_property.id}/photos")
        properties_repo.add_media(db, db_property.id, url, MediaType.PHOTO)

    for doc in ownership_docs:
        url = upload_file_to_s3(doc, folder=f"properties/{db_property.id}/docs")
        properties_repo.add_media(db, db_property.id, url, MediaType.OWNERSHIP_DOC)
    
    db.commit()
    db.refresh(db_property)
    
    background_tasks.add_task(run_verification_panel_task_sync, db_property.id)
    
    return db_property


