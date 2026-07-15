
from src.models.base import TimeStampedBase
from sqlalchemy import Column, String, Integer, Enum, ForeignKey
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship
import enum


class Property_status(str, enum.Enum):
    PENDING = "Pending"
    APPROVED = "Approved"
    REJECTED = "Rejected"
    CHANGES_NEEDED = "Changes needed"


class Properties(TimeStampedBase):
    __tablename__ = "properties_table"

    owner_id = Column(UUID(as_uuid=True), nullable=False, index=True)
    title = Column(String, nullable=False)
    description = Column(String, nullable=False)
    location = Column(String, nullable=False, index=True)
    rent_amount = Column(Integer, nullable=False)
    amenities = Column(String, nullable=False)
    status = Column(Enum(Property_status), default=Property_status.PENDING, index=True)
    
    media = relationship("PropertyMedia", back_populates="property", cascade="all, delete-orphan")
    
