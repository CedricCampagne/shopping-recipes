export interface Recipe {
  id: number;
  name: string;
  description: string;
  servings: number;
  imageUrl?: string | null;
}
