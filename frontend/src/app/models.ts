export type Status = 'WISHLIST' | 'APPLIED' | 'INTERVIEW' | 'OFFER' | 'REJECTED';

export interface JobApplication {
  id?: number;
  company: string;
  role: string;
  location?: string;
  link?: string;
  status: Status;
  appliedOn?: string | null;
  notes?: string;
}

/** Board columns in display order. */
export const COLUMNS: { value: Status; label: string }[] = [
  { value: 'WISHLIST', label: 'Wishlist' },
  { value: 'APPLIED', label: 'Applied' },
  { value: 'INTERVIEW', label: 'Interview' },
  { value: 'OFFER', label: 'Offer' },
  { value: 'REJECTED', label: 'Rejected' }
];
