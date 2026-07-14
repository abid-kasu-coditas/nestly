from pydantic import BaseModel
from typing import Literal

class CompletenessFinding(BaseModel):
    is_complete: bool
    missing_or_weak_fields: list[str] = []
    notes: str


class ConsistencyFinding(BaseModel):
    is_consistent: bool
    mismatches: list[str] = []
    notes: str


class PricingFinding(BaseModel):
    verdict: Literal["underpriced", "fair", "overpriced", "insufficient_data"]
    comparable_avg_rent: float | None = None
    notes: str

class RedFlagFinding(BaseModel):
    risk_level: Literal["low", "medium", "high"]
    flags: list[str] = []
    notes: str


class PanelRecommendation(BaseModel):
    
    summary: str
    key_concerns: list[str]
    recommendation: Literal["approve", "minor_changes", "manual_review"]
    
