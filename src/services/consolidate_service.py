from src.services.reviewers_service import call_reviewer
from src.schemas.verificaion_schema import (CompletenessFinding, ConsistencyFinding, PricingFinding, RedFlagFinding, PanelRecommendation)

async def consolidate(completeness: CompletenessFinding, consistency: ConsistencyFinding,pricing: PricingFinding, red_flags: RedFlagFinding) -> PanelRecommendation:
    combined = {"completeness": completeness.model_dump(),"consistency": consistency.model_dump(),"pricing": pricing.model_dump(),"red_flags": red_flags.model_dump()}
    
    prompt = ("You are the final reviewer on a rental listing verification panel. You've received "
              "independent findings from 4 specialist reviewers below . Write a short summary , list key "
              "concerns (empty list if none), and give ONE overall recommendation: 'approve' if solid, "
              "'minor_changes' for small fixable issues , 'manual_review' for real red flags or major issues.")
    
    return await call_reviewer(prompt, str(combined), PanelRecommendation)
