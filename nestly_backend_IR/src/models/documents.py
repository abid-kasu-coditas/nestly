from src.models.base import TimeStampedBase
import enum
from sqlalchemy import Column , ForeignKey , String , Enum 
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.postgresql import UUID



class MediaType(str, enum.Enum):
    PHOTO = "Photo"
    OWNERSHIP_DOC = "OwnershipDoc"


class PropertyMedia(TimeStampedBase):
    __tablename__ = "property_media_table"

    property_id = Column(UUID(as_uuid=True), ForeignKey("properties_table.id"), nullable=False, index=True)
    file_url = Column(String, nullable=False)
    media_type = Column(Enum(MediaType), nullable=False)


    property = relationship("Properties", back_populates="media")
    
    