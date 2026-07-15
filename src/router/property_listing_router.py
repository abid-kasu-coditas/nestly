from fastapi import APIRouter, Depends, UploadFile, File, Form, BackgroundTasks
from sqlalchemy.orm import Session
from src.db.session import get_db
from src.services.properties_service import create_listing
from src.schemas.properties_schema import Properties_response_schema
from uuid import UUID


router = APIRouter(prefix="/properties", tags=["Properties"])

@router.post("/", response_model=Properties_response_schema)
async def create_listing_endpoint(
    background_tasks: BackgroundTasks,
    title: str = Form(...),
    description: str = Form(...),
    location: str = Form(...),
    rent_amount: int = Form(...),
    amenities: str = Form(...),
    photos: list[UploadFile] = File(...),
    ownership_docs: list[UploadFile] = File(...),
    db: Session = Depends(get_db),
    current_user : UUID =Form(...)
):
    db_property = create_listing(
        db=db,
        owner_id=current_user.id,
        title=title,
        description=description,
        location=location,
        rent_amount=rent_amount,
        amenities=amenities,
        photos=photos,
        ownership_docs=ownership_docs,
        background_tasks=background_tasks,
    )
    return db_property



    
    












