from sqlalchemy import create_engine
from src.core.config import settings
from sqlalchemy.orm import sessionmaker


engine = create_engine(url= settings.DATABASE_URL , pool_pre_ping= True , pool_size= 10, max_overflow= 20)

session_local = sessionmaker(bind= engine , autoflush= False , autocommit = False)



def get_db():
    db = session_local()
    
    try:
        yield db
    finally:
        db.close()
        
    