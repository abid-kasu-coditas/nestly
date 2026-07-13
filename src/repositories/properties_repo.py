from sqlalchemy.orm import Session
from src.schemas.properties_schema import Property_list_schema , Properties_response_schema
from src.models.properties import Properties , Property_status 
from uuid import UUID
from fastapi import UploadFile , HTTPException
import os
from src.core.config import settings

def apply_for_listing(db : Session ,property : Property_list_schema , photo : ) -> Properties_response_schema:
    
    #check if the property exist and its status is not equal to approved
    
    property_to_be_approved = db.query(Properties).filter(property.tittle == Properties.tittle).first()
    
    if property_to_be_approved.status == Property_status.PENDING:
        
        db_property = Properties(tittle = property.tittle,
        description = property.description,
        Location = property.Location,
        rent_amount = property.rent_amount ,
        amenities = property.amenities)
    
        db.add(db_property)
        db.flush()
        
        
        
        #photo to upload
        if photo and photo.filename:
            ext = photo.filename.rsplit(".", 1)[-1].lower()
        if ext not in {"pdf", "jpg", "png", "docx"}:
            db.rollback()
            raise HTTPException(status_code=400, detail="Invalid file type. Allowed: pdf, jpg, png, docx")

        os.makedirs(settings.LOCAL_UPLOADS_DIR, exist_ok=True)
        

        
        temp_file_path = os.path.join(settings.LOCAL_UPLOADS_DIR, photo.filename)
    

        pass