import { readableApiError } from '../../shared/src';
describe('mobile error mapping', () => { it('keeps API messages visible to the rider', () => expect(readableApiError({message:'Session expired',status:401})).toEqual({message:'Session expired',status:401})); });
