import { describe, expect, it } from "vitest";
import  { normalizeText} from "./string.utils"

describe('normelizeTest', ()=>{
    it('should normelize text', ()=> {
        expect(normalizeText('Éléphant')).toBe('elephant');
    });

    it('should remove accents', () => {
        expect(normalizeText('Crème brûlée')).toBe('creme brulee');
    });

    it('should convert œ to oe', () => {
        expect(normalizeText('cœur')).toBe('coeur');
    });

    it('should convert æ to ae', () => {
        expect(normalizeText('cæur')).toBe('caeur');
    });

    it('should convert uppercase text to lowercase', () => {
        expect(normalizeText('ELEPHANT')).toBe('elephant');
    });

    it('should handle an empty string', () => {
        expect(normalizeText('')).toBe('');
    });
});