from functools import lru_cache
from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    
    
    DATABASE_URL: str
    
    
    AWS_BUCKET_NAME : str
    AWS_ACCESS_KEY_ID : str
    AWS_SECRET_ACCESS_KEY : str
    AWS_DEFAULT_REGION : str
    
    
    GROQ_API_KEY : str
    
    class Config:
        env_file = ".env"
        case_sensitive = True
    

@lru_cache(maxsize=1)
def get_settings():
    return Settings()


settings = get_settings()


