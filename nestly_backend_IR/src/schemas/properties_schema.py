from pydantic import BaseModel
from src.models.properties import Property_status
from uuid import UUID
from datetime import datetime



class Property_list_schema(BaseModel):
    
    tittle : str
    description : str
    Location : str
    rent_amount : int
    amenities : str
        
        

class Properties_response_schema(BaseModel):
    
    id : UUID
    created_at : datetime
    updated_at : datetime
    
    tittle : str
    description : str
    Location : str
    rent_amount : int
    amenities : str

    
    
    class Config:
        from_attributes = True
        
    