from sqlalchemy.orm import Session
from src.models.properties import Properties, PropertyMedia, Property_status, MediaType
from uuid import UUID

def create_property(db: Session, owner_id: UUID, title: str, description: str,location: str, rent_amount: int, amenities: str) -> Properties:
    db_property = Properties( owner_id=owner_id,title=title,description=description,location=location,
        rent_amount=rent_amount, amenities=amenities,
        status=Property_status.PENDING)
    db.add(db_property)
    db.flush()
    return db_property

def add_media(db: Session, property_id: UUID, file_url: str, media_type: MediaType) -> PropertyMedia:
    media = PropertyMedia(property_id=property_id, file_url=file_url, media_type=media_type)
    db.add(media)
    return media

def get_comparable_rents(db: Session, location: str, exclude_id: UUID | None = None, limit: int = 10) -> list[int]:
    rent = db.query(Properties.rent_amount).filter(Properties.location == location,Properties.status == Property_status.APPROVED)
    if exclude_id:
        rent = rent.filter(Properties.id != exclude_id)
    return [r[0] for r in rent.limit(limit).all()]

def get_property_by_id(db: Session, property_id: UUID) -> Properties | None:
    return db.query(Properties).filter(Properties.id == property_id).first()
