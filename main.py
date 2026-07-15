from fastapi import FastAPI
from src.router import property_listing_router
from src.models.documents import PropertyMedia
from src.models.properties import Properties
from src.models.report import VerificationReport



app = FastAPI()


app.include_router(property_listing_router.router)