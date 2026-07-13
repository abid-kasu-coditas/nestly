from functools import lru_cache
from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    
    
    DATABASE_URL : str
    
    
    
    class Config:
        env_file = ".env"
        case_sensitive = True
    

@lru_cache(maxsize=1)
def get_settings():
    return Settings


settings = get_settings()


