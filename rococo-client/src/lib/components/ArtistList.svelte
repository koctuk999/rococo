<script lang="ts">
	import { Avatar } from "@skeletonlabs/skeleton";
    import { artistsStore } from '$lib/stores/artist.store';

</script>

<ul class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4 p-8 qa-artist-items">
    {#each $artistsStore.data as artist(artist.id)}
        <li class="qa-artist-item">
            <a href={`/artist/${artist.id}`} class="flex flex-col justify-center items-center">
                <Avatar src={artist.photo} width="w-48" rounded="rounded-full" />
                <span class="flex-auto py-4">{artist.name}</span>
            </a>
        </li>
    {/each}
</ul>
