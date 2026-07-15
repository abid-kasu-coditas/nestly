from groq import AsyncGroq
from src.core.config import settings
from src.schemas.verificaion_schema import (CompletenessFinding, ConsistencyFinding, PricingFinding, RedFlagFinding)

client = AsyncGroq(api_key=settings.GROQ_API_KEY)

MODEL = "llama-3.3-70b-versatile"

async def call_reviewer(system_prompt: str, listing_json: str, schema_model):
    response = await client.chat.completions.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": system_prompt + f"\nRespond ONLY with valid JSON matching this schema: {schema_model.model_json_schema()}"},
            {"role": "user", "content": listing_json},
        ],
        response_format={"type": "json_object"},
        temperature=0.2,
    )
    return schema_model.model_validate_json(response.choices[0].message.content)

async def review_completeness(listing_json: str) -> CompletenessFinding:
    prompt = ("You review rental listings for completeness.Check whether title, description, "
              "location, rent, amenities, and photo count are all present and make sense together. "
              "Be specific about what's missing or thin, not generic.")
    return await call_reviewer(prompt, listing_json, CompletenessFinding)



async def review_consistency(listing_json: str) -> ConsistencyFinding:
    prompt = ("You check whether a rental listing's free-text description matches its structured "
              "details (room count, amenities). Flag any specific claim in the description that "
              "contradicts or isn't supported by the structured fields.")
    
    return await call_reviewer(prompt, listing_json, ConsistencyFinding)


async def review_pricing(listing_json: str) -> PricingFinding:
    prompt = ("You compare a listing's rent against comparable listings provided in the same location. "
              "Given the listing and a list of comparable rents, decide if it's fairly priced,"
              "underpriced, or overpriced. If no comparables are given, say insufficient_data.")
    
    
    return await call_reviewer(prompt, listing_json, PricingFinding)



async def review_red_flags(listing_json: str) -> RedFlagFinding:
    prompt = ("You look for red flags in how a rental listing is written: vague/generic descriptions,"
              "urgency or pressure language, wording that doesn't match a genuine ready-to-rent property."
              "Be specific about phrases, don't guess.")
    
    return await call_reviewer(prompt, listing_json, RedFlagFinding)
