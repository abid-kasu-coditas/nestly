from src.models.base import TimeStampedBase
from sqlalchemy import Column , String , Integer , Enum
import enum




class Property_status(str , enum.Enum):
    
    PENDING = "Pending"
    APPROVED = "Approved"
    REJECTED = "Rejected"
    CHANGES_NEEDED = "Changes needed"
    
    
    
class Properties(TimeStampedBase):
    
    __tablename__ = "properties_table"
    
    
    tittle = Column(String , nullable= False)
    description = Column(String , nullable= False)
    Location = Column(String , nullable=False , index= True)
    rent_amount = Column(Integer , nullable= False)
    amenities = Column(String , nullable= False)
    Photos = Column(String , nullable= False)
    ownership_document = Column(String , nullable= False)
    status = Column(Enum , default= Property_status.PENDING , index= True)
    
    

    