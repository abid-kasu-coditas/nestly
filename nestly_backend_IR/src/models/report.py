from src.models.base import TimeStampedBase
from sqlalchemy import Column, String, ForeignKey, Enum, JSON
from sqlalchemy.dialects.postgresql import UUID
import enum

class ReportStatus(str, enum.Enum):
    IN_PROGRESS = "InProgress"
    COMPLETED = "Completed"

class VerificationReport(TimeStampedBase):
    __tablename__ = "verification_reports_table"

    property_id = Column(UUID(as_uuid=True), ForeignKey("properties_table.id"), nullable=False, index=True)
    completeness = Column(JSON, nullable=True)
    consistency = Column(JSON, nullable=True)
    pricing = Column(JSON, nullable=True)
    red_flags = Column(JSON, nullable=True)
    summary = Column(String, nullable=True)
    key_concerns = Column(JSON, nullable=True)
    recommendation = Column(String, nullable=True)
    status = Column(Enum(ReportStatus), default=ReportStatus.IN_PROGRESS, index=True)

